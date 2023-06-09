package org.localmc.tools.ftbqkeys.command;

import com.feed_the_beast.ftbquests.FTBQuests;
import com.feed_the_beast.ftbquests.quest.Chapter;
import com.feed_the_beast.ftbquests.quest.ChapterImage;
import com.feed_the_beast.ftbquests.quest.Quest;
import com.feed_the_beast.ftbquests.quest.QuestFile;
import com.feed_the_beast.ftbquests.quest.loot.RewardTable;
import com.feed_the_beast.ftbquests.quest.reward.Reward;
import com.feed_the_beast.ftbquests.quest.task.Task;
import com.google.common.collect.Lists;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import org.apache.commons.io.FileUtils;
import org.localmc.tools.ftbqkeys.FTBQKeysModLegacy;

import java.io.File;
import java.util.List;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FTBQKeysConvCommand extends CommandBase {
    @Override
    public String getName() {
        return "lang";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.ftbqkeys.lang.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        try {
            File parent = new File(FTBQKeysModLegacy.gameDir.toFile(), "ftbqlocalizationkeys");
            File transFiles = new File(parent, "kubejs/assets/kubejs/lang/");
            File questsFolder = new File(FTBQKeysModLegacy.configDir.toFile(), "config/ftbquests/");

            if (questsFolder.exists()) {
                File backup = new File(parent, "backup/ftbquests");
                FileUtils.copyDirectory(questsFolder, backup);
            }

            TreeMap<String, String> transKeys = new TreeMap<>();
            QuestFile file = FTBQuests.PROXY.getQuestFile(false);

            for (int i = 0; i < file.rewardTables.size(); i++) {
                RewardTable table = file.rewardTables.get(i);

                transKeys.put("loot_table." + (i + 1), table.title);
                table.title = "{" + "loot_table." + (i + 1) + "}";
            }

            for (int i = 0; i < file.chapterGroups.size(); i++) {
                ChapterGroup chapterGroup = file.chapterGroups.get(i);

                if (!chapterGroup.title.isEmpty()) {
                    transKeys.put("category." + (i + 1), chapterGroup.title);
                    chapterGroup.title = "{" + "category." + (i + 1) + "}";
                }
            }

            for (int i = 0; i < file.getAllChapters().size(); i++) {
                Chapter chapter = file.getAllChapters().get(i);

                String prefix = "chapter." + (i + 1);

                if (!chapter.title.isEmpty()) {
                    transKeys.put(prefix + ".title", chapter.title);
                    chapter.title = "{" + prefix + ".title" + "}";
                }

                if (chapter.subtitle.size() > 0) {
                    transKeys.put(prefix + ".subtitle", String.join("\n", chapter.subtitle));
                    chapter.subtitle.clear();
                    chapter.subtitle.add("{" + prefix + ".subtitle" + "}");
                }


                for (int i1 = 0; i1 < chapter.images.size(); i1++) {
                    ChapterImage chapterImage = chapter.images.get(i1);

                    if (!chapterImage.hover.isEmpty()) {
                        transKeys.put(prefix + ".image." + (i1 + 1), String.join("\n", chapterImage.hover));
                        chapterImage.hover.clear();
                        chapterImage.hover.add("{" + prefix + ".image." + (i1 + 1) + "}");
                    }
                }

                for (int i1 = 0; i1 < chapter.quests.size(); i1++) {
                    Quest quest = chapter.quests.get(i1);

                    if (!quest.title.isEmpty()) {
                        transKeys.put(prefix + ".quest." + (i1 + 1) + ".title", quest.title);
                        quest.title = "{" + prefix + ".quest." + (i1 + 1) + ".title}";
                    }

                    if (!quest.subtitle.isEmpty()) {
                        transKeys.put(prefix + ".quest." + (i1 + 1) + ".subtitle", quest.subtitle);
                        quest.subtitle = "{" + prefix + ".quest." + (i1 + 1) + ".subtitle" + "}";
                    }

                    if (quest.description.size() > 0) {
                        List<String> descList = Lists.newArrayList();

                        StringJoiner joiner = new StringJoiner("\n");
                        int num = 1;

                        for (int i2 = 0; i2 < quest.description.size(); i2++) {
                            String desc = quest.description.get(i2);

                            final String regex = "\\{image:.*?}";

                            if (desc.contains("{image:")) {
                                if (!joiner.toString().isEmpty()) {
                                    transKeys.put(prefix + ".quest." + (i1 + 1) + ".description." + num, joiner.toString());
                                    descList.add("{" + prefix + ".quest." + (i1 + 1) + ".description." + num + "}");
                                    joiner = new StringJoiner("\n");
                                    num++;
                                }

                                final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
                                final Matcher matcher = pattern.matcher(desc);

                                while (matcher.find()) {
                                    desc = desc.replace(matcher.group(0), "");
                                    descList.add(matcher.group(0));
                                }
                            } else {
                                if (desc.isEmpty()) {
                                    joiner.add("\n");
                                } else {
                                    joiner.add(desc);
                                }
                            }
                        }

                        if (!joiner.toString().isEmpty()) {
                            transKeys.put(prefix + ".quest." + (i1 + 1) + ".description." + num, joiner.toString());
                            descList.add("{" + prefix + ".quest." + (i1 + 1) + ".description." + num + "}");
                        }

                        quest.description.clear();
                        quest.description.addAll(descList);
                    }

                    for (int i2 = 0; i2 < quest.tasks.size(); i2++) {
                        Task task = quest.tasks.get(i2);

                        if (!task.title.isEmpty()) {
                            transKeys.put(prefix + ".quest." + (i1 + 1) + ".task." + (i2 + 1) + ".title", task.title);
                            task.title = "{" + prefix + ".quest." + (i1 + 1) + ".task." + (i2 + 1) + ".title}";
                        }
                    }

                    for (int i2 = 0; i2 < quest.rewards.size(); i2++) {
                        Reward reward = quest.rewards.get(i2);

                        if (!reward.title.isEmpty()) {
                            transKeys.put(prefix + ".quest." + (i1 + 1) + ".reward." + (i2 + 1) + ".title", reward.title);
                            reward.title = "{" + prefix + ".quest." + (i1 + 1) + ".reward." + (i2 + 1) + ".title}";
                        }
                    }
                }
            }

            File output = new File(parent, "config/ftbquests");

            file.writeDataFull(output.toPath().toFile());

            String lang = Ctx.getArgument("lang", String.class);
            FTBQKeysModLegacy.saveLang(transKeys, lang, transFiles);

            if (!lang.equalsIgnoreCase("en_us")) {
                FTBQKeysModLegacy.saveLang(transKeys, "en_us", transFiles);
            }

            sender.sendMessage(new TextComponentTranslation("command.ftbqkeys.message" + parent.getAbsolutePath()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
